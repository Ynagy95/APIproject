package proj.utils.report;

import org.jsoup.Jsoup;
import proj.utils.OperatingSystem;
import proj.utils.TerminalUtils;
import proj.utils.logs.LogsManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AllureBinaryManager {

    private static class Holder {
        static final String VERSION = resolveVersion(); // e.g., 2.35.1

        private static String resolveVersion() {
            try {
                String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest")
                        .followRedirects(true)
                        .execute()
                        .url()
                        .toString();
                return url.split("tag/")[1];
            } catch (Exception e) {
                LogsManager.warn("⚠️ Unable to fetch latest Allure version, using fallback 2.35.1");
                return "2.35.1";
            }
        }
    }

    public static void downloadAndExtract() {
        try {
            String version = Holder.VERSION;
            Path extractionDir = AllureConstants.EXTRACTION_DIR.resolve("allure-" + version);
            Path executable = getExecutable();

            if (Files.exists(executable)) {
                LogsManager.info("✅ Allure binary already exists: " + executable);
                return;
            }

            Files.createDirectories(AllureConstants.EXTRACTION_DIR);
            Path zipPath = downloadZip(version);
            if (!Files.exists(zipPath)) {
                LogsManager.error("❌ Allure ZIP not found after download: " + zipPath);
                return;
            }

            extractZipFile(zipPath);
            LogsManager.info("✅ Allure binaries extracted to: " + extractionDir);

            // Set execute permissions (non-Windows)
            if (OperatingSystem.getOperatingSystemType() != OperatingSystem.OSType.WINDOWS) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", executable.toString());
            }

            // Delete ZIP file after extraction
            Files.deleteIfExists(zipPath);

        } catch (Exception e) {
            LogsManager.error("⚠️ Error downloading or extracting Allure binaries", e.getMessage());
        }
    }

    public static Path getExecutable() {
        String version = Holder.VERSION;
        return AllureConstants.EXTRACTION_DIR.resolve("allure-" + version)
                .resolve("bin")
                .resolve(OperatingSystem.getOperatingSystemType() == OperatingSystem.OSType.WINDOWS
                        ? "allure.bat"
                        : "allure");
    }

    private static Path downloadZip(String version) {
        try {
            String url = AllureConstants.ALLURE_ZIP_BASE_URL + version +
                    "/allure-commandline-" + version + ".zip";
            Path zipFile = AllureConstants.EXTRACTION_DIR.resolve("allure-" + version + ".zip");

            LogsManager.info("⬇️ Downloading Allure from: " + url);

            try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
                 OutputStream out = Files.newOutputStream(zipFile)) {
                in.transferTo(out);
            }

            LogsManager.info("✅ Downloaded Allure ZIP: " + zipFile);
            return zipFile;

        } catch (Exception e) {
            LogsManager.error("❌ Failed to download Allure ZIP", e.getMessage());
            return Paths.get("");
        }
    }

    private static void extractZipFile(Path zipPath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path filePath = AllureConstants.EXTRACTION_DIR.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }
        }
    }
}
