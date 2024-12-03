/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Service
public class TemplateService {

    private final Configuration configuration;

    public TemplateService() {
        configuration = new Configuration(Configuration.VERSION_2_3_33);

        configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
    }

    public String applyTemplate(Map<String, Object> root, String templatePath) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templatePath);

        Writer out = new StringWriter();
        template.process(root, out);

        return out.toString();
    }

}
