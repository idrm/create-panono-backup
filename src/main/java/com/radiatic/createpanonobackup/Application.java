package com.radiatic.createpanonobackup;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

public class Application {

	@Parameter(names={"--username", "-u"})
	String username;

	@Parameter(names={"--password", "-p"})
	String password;

	@Parameter(names={"--includeUpf", "-upf"})
	String downloadUpf;

	BasicCookieStore cookieStore = new BasicCookieStore();
	HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

	public static void main(String ...argv) {
		Application main = new Application();
		JCommander.newBuilder()
			.addObject(main)
			.build()
			.parse(argv);
		main.run();
	}

	private void downloadImage(String url, File destFile) throws Exception {
		final HttpGet fetchImage = new HttpGet(url);
		fetchImage.setHeader("Referer", "https://cloud.panono.com/p/1111111111111111");

		CloseableHttpResponse response;
		try {
			response = (CloseableHttpResponse)client.execute(fetchImage);
		} catch (Exception ex) {
			throw new Exception("Fetch panorama image request error: " + ex.getMessage());
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			try {
				throw new Exception("Fetch panorama image request error: " + EntityUtils.toString(response.getEntity()));
			} catch (Exception ex) {
				throw new Exception("Fetch panorama image request error");
			}
		}

		byte[] bytes = EntityUtils.toByteArray(response.getEntity());
		Files.write(
			Paths.get(destFile.getPath()),
			bytes,
			StandardOpenOption.CREATE_NEW
		);

	}

	private String fetchPanoramas(String pageSize, String offset) throws Exception {
		final HttpGet fetchPanoramasRequest = new HttpGet(
			String.format("https://api3-dev.panono.com/u/%s/panoramas?pageSize=%s", username, pageSize)
			+ (offset != null ? "&offset=" + offset : "")
		);
		fetchPanoramasRequest.setHeader("Referer", String.format("https://cloud.panono.com/u/%s/all/overview", username));
		fetchPanoramasRequest.setHeader("Origin", "https://cloud.panono.com");

		CloseableHttpResponse response;
		try {
			response = (CloseableHttpResponse)client.execute(fetchPanoramasRequest);
		} catch (Exception ex) {
			throw new Exception("Fetch panoramas request error: " + ex.getMessage());
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			try {
				throw new Exception("Fetch panoramas request error: " + EntityUtils.toString(response.getEntity()));
			} catch (Exception ex) {
				throw new Exception("Fetch panoramas request error");
			}
		}

		return EntityUtils.toString(response.getEntity());
	}

	private String fetchPanorama(String id) throws Exception {
		final HttpGet fetchPanoramasRequest = new HttpGet(
			String.format("https://api3-dev.panono.com/panorama/%s", id)
		);
		fetchPanoramasRequest.setHeader("Referer", String.format("ttps://cloud.panono.com/p/%s", id));
		fetchPanoramasRequest.setHeader("Origin", "https://cloud.panono.com");

		CloseableHttpResponse response;
		try {
			response = (CloseableHttpResponse)client.execute(fetchPanoramasRequest);
		} catch (Exception ex) {
			throw new Exception("Fetch panorama request error: " + ex.getMessage());
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			try {
				throw new Exception("Fetch panorama request error: " + EntityUtils.toString(response.getEntity()));
			} catch (Exception ex) {
				throw new Exception("Fetch panorama request error");
			}
		}

		return EntityUtils.toString(response.getEntity());
	}

	@SuppressWarnings("unchecked")
	public void run() {
		if (username == null || password == null) {
			System.out.println("Either the Panono account username or the password was not specified");
			System.out.println("Usage: java -jar create-panono-backup-1.1.jar --username \"USERNAME\" --password \"PASSWORD\" --downloadUpf UPF_FLAG");
			System.out.println("where UPF_FLAG should be specified as \"yes\" to also download the UPF files");
			return;
		}

		ObjectMapper mapper = new ObjectMapper();

		final HttpPost loginRequest = new HttpPost("https://api3-dev.panono.com/login");

		String loginJson = "";
		try {
			loginJson = mapper.writeValueAsString(
				ImmutableMap.of(
					"email", username,
					"password", password,
					"remember_me", false
				)
			);
		} catch (Exception ignored) {}

		try {
			loginRequest.setEntity(new StringEntity(loginJson));
		} catch (Exception ex) {
			System.out.println("Login request error: " + ex.getMessage());
			return;
		}
		loginRequest.setHeader("Referer", "https://cloud.panono.com/account/log-in");
		loginRequest.setHeader("Origin", "https://cloud.panono.com");
		loginRequest.setHeader("Accept", "application/json");
		loginRequest.setHeader("Content-type", "application/json");

		CloseableHttpResponse response;
		try {
			response = (CloseableHttpResponse)client.execute(loginRequest);
		} catch (Exception ex) {
			System.out.println("Login request error: " + ex.getMessage());
			return;
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Failed to log into Panono. Did you enter the correct username and password?");
			return;
		}

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		File cwd = new File(s);

		File backupFolder = new File(String.format("%s/panono-backups/%s", cwd.getPath(), username));

		if (!backupFolder.exists())
			backupFolder.mkdirs();

		List<Panorama> panoramas = new ArrayList<>();

		String offset = null;
		String pageSize = "12";
		while (true) {
			System.out.printf("Fetching panoramas with offset [%s]\r\n", offset != null ? offset : "-");

			String panoramasJson;
			try {
				panoramasJson = fetchPanoramas(pageSize, offset);
			} catch (Exception ex) {
				System.out.println("Failed to fetch panoramas: " + ex.getMessage());
				break;
			}

			try {
				Map panoramasMap = mapper.readValue(panoramasJson, Map.class);

				offset = (String)panoramasMap.getOrDefault("offset", null);
				List items = (List)panoramasMap.getOrDefault("items", null);

				items.forEach(itemRaw -> {
					Map item = (Map) itemRaw;
					String type = (String)item.getOrDefault("type", null);

					if ("panorama".equals(type)) {
						String id = (String) item.getOrDefault("id", null);
						Map data = (Map) item.getOrDefault("data", null);
						String title = (String) data.getOrDefault("title", null);
						String description = (String) data.getOrDefault("description", null);
						DateTime createdAt = new DateTime(data.getOrDefault("created_at", null)).withZone(DateUtil.tzUtc);
						panoramas.add(new Panorama(id, title, createdAt, description));
					}
				});

				if (offset == null) {
					break;
				}

				pageSize = "50";
			} catch (Exception ex) {
				System.out.println("Failed to fetch panoramas due to unrecognized server response");
				break;
			}
		}

		AtomicInteger panoCounter = new AtomicInteger(0);

		List<Panorama> newPanoramas = panoramas
			.stream()
			.filter(pano -> {
				File panoFolder = new File(String.format("%s/%s", backupFolder.getPath(), pano.getId()));
				File panoramaInfoFile = new File(String.format("%s/%s.txt", panoFolder.getPath(), pano.getId()));

				return !panoramaInfoFile.exists();
			})
			.collect(toList());

		System.out.printf("Found %d non-synced panoramas out of %d total\r\n", newPanoramas.size(), panoramas.size());

		for (Panorama pano : newPanoramas) {
			File panoFolder = new File(String.format("%s/%s", backupFolder.getPath(), pano.getId()));
			if (!panoFolder.exists())
				panoFolder.mkdirs();

			System.out.printf("Downloading panorama [%s] (%s) (%d / %d)...\r\n", pano.getTitle(), pano.getId(), panoCounter.incrementAndGet(), newPanoramas.size());

			String panoramaJson;
			try {
				panoramaJson = fetchPanorama(pano.getId());
			} catch (Exception ex) {
				System.out.println("Failed to fetch panorama: " + ex.getMessage());
				return;
			}

			Map panoramaMap;
			try {
				panoramaMap = mapper.readValue(panoramaJson, Map.class);
			} catch (Exception ex) {
				System.out.println("Invalid panorama data: " + ex.getMessage());
				return;
			}

			Map data = (Map) panoramaMap.getOrDefault("data", null);

			ImmutableMap.Builder imageUrlsBuilder = ImmutableMap.<File, String>builder();

			Map sourceUpf = (Map)data.getOrDefault("source_upf", null);
			String upfUrl = (String)sourceUpf.getOrDefault("url", null);

			if (downloadUpf != null && "yes".equals(downloadUpf.toLowerCase()))
				imageUrlsBuilder.put(
					new File(String.format("%s/%s.upf", panoFolder.getPath(), pano.getId())),
					upfUrl
				);

			Map images = (Map)data.getOrDefault("images", null);
			List equirectangulars = (List)images.getOrDefault("equirectangulars", null);

			equirectangulars.forEach(equirectangularRaw -> {
				Map equirectangular = (Map)equirectangularRaw;
				Integer width = (Integer)equirectangular.getOrDefault("width", null);
				String url = (String)equirectangular.getOrDefault("url", null);

				imageUrlsBuilder.put(
					new File(String.format("%s/%s-%d.jpg", panoFolder.getPath(), pano.getId(), width)),
					url
				);
			});

			Map<File, String> imageUrls = imageUrlsBuilder.build();

			Set<File> files = imageUrls.keySet();

			for (File file : files) {
				String imageUrl = imageUrls.get(file);
				try {
					System.out.printf("Downloading %s...\r\n", imageUrl);
					downloadImage(imageUrl, file);
				} catch (Exception ex) {
					System.out.printf("!!! Failed to download %s\r\n", imageUrl);
					return;
				}
			}

			File panoramaInfoFile = new File(String.format("%s/%s.txt", panoFolder.getPath(), pano.getId()));

			try {
				Files.write(
					Paths.get(panoramaInfoFile.getPath()),
					String.join(
						"\r\n",
						ImmutableList.of(
							String.format("id=%s", pano.getId()),
							String.format("title=%s", pano.getTitle()),
							String.format("description=%s", pano.getDescription() != null ? pano.getDescription() : ""),
							String.format("createdAt=%s", pano.getCreatedAt().toString())
						)
					).getBytes(),
					StandardOpenOption.CREATE_NEW
				);
			} catch (Exception ex) {
				System.out.println("Failed to create panorama info file");
				return;
			}
		}

		File panoramasInfoFile = new File(String.format("%s/panoramas.txt", backupFolder.getPath()));

		try {
			Files.write(
				Paths.get(panoramasInfoFile.getPath()),
				String.join(
					"\r\n",
					panoramas.stream().map(pano -> String.format("%s=%s", pano.getId(), pano.getTitle() != null ? pano.getTitle() : "")).collect(toList())
				).getBytes(),
				StandardOpenOption.CREATE_NEW
			);
		} catch (Exception ex) {
			System.out.println("Could not write the panoramas.txt file");
		}

		System.out.println ("Done backing up Panono panoramas");

		try {
			response.close();
		} catch (Exception ignored) {}

	}
}
