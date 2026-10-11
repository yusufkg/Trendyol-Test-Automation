package com.yusuf.qa.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/** Generates a self-contained summary after every Cucumber scenario. */
public final class HtmlDashboard {
    private HtmlDashboard() {}
    private record Result(String name, String status, long duration, String screenshot, String error, String sqlStatus) {}
    private static final List<Result> RESULTS = new ArrayList<>();
    private static final String RUN_STARTED = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"))
        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss XXX"));

    public static synchronized void record(String name, String status, long duration, String screenshot, String error, String sqlStatus) throws IOException {
        RESULTS.add(new Result(name, status, duration, screenshot, error, sqlStatus));
        int passed = (int) RESULTS.stream().filter(r -> "PASSED".equals(r.status)).count();
        int failed = (int) RESULTS.stream().filter(r -> "FAILED".equals(r.status)).count();
        int other = RESULTS.size() - passed - failed;
        long saved = RESULTS.stream().filter(r -> "SAVED".equals(r.sqlStatus)).count();
        long sqlFailed = RESULTS.stream().filter(r -> "FAILED".equals(r.sqlStatus)).count();
        long unconfigured = RESULTS.stream().filter(r -> "NOT_CONFIGURED".equals(r.sqlStatus)).count();
        StringBuilder html = new StringBuilder("""
            <!doctype html><html lang="tr"><head><meta charset="utf-8"><title>QA Test Dashboard</title>
            <style>body{font:16px system-ui;margin:36px auto;max-width:960px;color:#172033;background:#f6f8fc}
            .cards{display:flex;gap:12px;flex-wrap:wrap}.card{background:white;border-radius:12px;padding:20px;flex:1;min-width:130px;box-shadow:0 2px 12px #dce1e8}
            .num{font-size:30px;font-weight:700}.bar{height:24px;background:#e3e8f1;border-radius:12px;overflow:hidden;margin:24px 0}
            .ok{height:100%;background:#15946b}table{width:100%;border-collapse:collapse;background:white}th,td{padding:12px;text-align:left;border-bottom:1px solid #e5e9f0}
            .pass{color:#147d5b;font-weight:bold}.fail{color:#b32a3d;font-weight:bold}</style></head><body>
            <h1>Otomasyon Test Raporu</h1><p>JUnit ve Cucumber test sonuçları • son çalıştırma</p>
            """);
        html.append("<p>Başlangıç (İstanbul): ").append(escape(RUN_STARTED)).append("</p>");
        html.append("<p>SQL'e kaydedilen: <strong>").append(saved)
            .append("</strong> · SQL kayıt hatası: <strong>").append(sqlFailed)
            .append("</strong> · SQL yapılandırılmamış: <strong>").append(unconfigured).append("</strong></p>");
        html.append("<div class='cards'>");
        for (String[] item : new String[][]{{"Toplam", ""+RESULTS.size()}, {"Başarılı", ""+passed}, {"Başarısız", ""+failed}, {"Diğer", ""+other}}) {
            html.append("<div class='card'><div>").append(item[0]).append("</div><div class='num'>").append(item[1]).append("</div></div>");
        }
        html.append("</div><div class='bar'><div class='ok' style='width:")
            .append(RESULTS.isEmpty()?0:100*passed/RESULTS.size()).append("%'></div></div>");
        html.append("<table><thead><tr><th>Test</th><th>Durum</th><th>Süre (ms)</th><th>SQL kayıt</th><th>Kanıt</th></tr></thead><tbody>");
        for (Result r : RESULTS) {
            html.append("<tr><td>").append(escape(r.name)).append("</td><td class='")
                .append("PASSED".equals(r.status)?"pass":"fail").append("'>")
                .append(escape(r.status)).append("</td><td>").append(r.duration).append("</td><td>").append(escape(r.sqlStatus)).append("</td><td>");
            if (r.screenshot != null) html.append("<a href='").append(escape(r.screenshot.replace('\\','/'))).append("'>Görüntü</a>");
            if (r.error != null) html.append("<details><summary>Hata ayrıntısı</summary><pre style='white-space:pre-wrap;overflow-wrap:anywhere'>").append(escape(r.error)).append("</pre></details>");
            html.append("</td></tr>");
        }
        html.append("</tbody></table>");
        if (Files.exists(Path.of("target", "ecommerce-report.html")))
            html.append("<p><a href='ecommerce-report.html'>Cucumber adım raporu (son senaryo çalıştırması)</a></p>");
        html.append("<p>Bu özet yalnızca çalıştırılan testleri içerir. Gherkin tasarımlarının tamamının uygulanmış veya geçmiş olduğu anlamına gelmez.</p></body></html>");
        Path target = Path.of("target", "qa-dashboard.html");
        Files.createDirectories(target.getParent());
        Files.writeString(target, html.toString(), StandardCharsets.UTF_8);
    }
    private static String escape(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");
    }
}
