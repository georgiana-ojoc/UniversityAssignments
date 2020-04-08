package dms.tool;

import dms.Main;
import dms.exception.ReportCommandException;
import dms.resource.*;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class HTMLReport {
    public static void create(String path, Catalog catalog) throws ReportCommandException {
        try (Writer fileWriter = new FileWriter(new File(path + "\\report.html"))) {
            Configuration config = new Configuration();
            config.setClassForTemplateLoading(Main.class, "templates");
            config.setDefaultEncoding("UTF-8");
            config.setLocale(Locale.US);
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            Template template = config.getTemplate("report.ftl");
            Map<String, Object> input = new HashMap<>();
            input.put("title", "HTML report");
            input.put("name", catalog.getName());
            input.put("path", catalog.getPath());
            List<Document> documents = new ArrayList<>(catalog.getDocuments());
            input.put("documents", documents);
            template.process(input, fileWriter);
        }
        catch (TemplateException | IOException e) {
            throw new ReportCommandException(e);
        }
    }
}
