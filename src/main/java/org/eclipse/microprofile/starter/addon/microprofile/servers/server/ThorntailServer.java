/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.eclipse.microprofile.starter.addon.microprofile.servers.server;

import org.apache.maven.model.Model;
import org.eclipse.microprofile.starter.addon.microprofile.servers.AbstractMicroprofileAddon;
import org.eclipse.microprofile.starter.addon.microprofile.servers.model.MicroprofileSpec;
import org.eclipse.microprofile.starter.addon.microprofile.servers.model.SupportedServer;
import org.eclipse.microprofile.starter.core.artifacts.MavenCreator;
import org.eclipse.microprofile.starter.core.model.JessieModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class ThorntailServer extends AbstractMicroprofileAddon {

    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    public String addonName() {
        return SupportedServer.THORNTAIL_V2.getCode();
    }

    @Override
    public void createFiles(JessieModel model) {
        Set<String> alternatives = model.getParameter(JessieModel.Parameter.ALTERNATIVES);
        Map<String, String> variables = model.getVariables();

        List<MicroprofileSpec> microprofileSpecs = model.getParameter(JessieModel.Parameter.MICROPROFILESPECS);
        if (model.hasMainAndSecondaryProject() && microprofileSpecs.contains(MicroprofileSpec.JWT_AUTH)) {

            // Specific files for Auth-JWT
            String resourceDirectory = getResourceDirectory(model, false);
            directoryCreator.createDirectory(resourceDirectory);
            processTemplateFile(resourceDirectory, "project-defaults.yml", alternatives, variables);
            processTemplateFile(resourceDirectory, "jwt-roles.properties", alternatives, variables);

            String metaInfDirectory = getResourceDirectory(model, false) + "/META-INF";

            directoryCreator.createDirectory(metaInfDirectory);
            processTemplateFile(metaInfDirectory, "publicKey.pem", "MP-JWT-SIGNER", alternatives, variables);

            /// web.xml required for WildFly swarm
            String webInfDirectory = model.getDirectory(false) + "/" + MavenCreator.SRC_MAIN_WEBAPP + "/WEB-INF";
            directoryCreator.createDirectory(webInfDirectory);

            String webXMLContents = thymeleafEngine.processFile("web.xml", alternatives, variables);
            fileCreator.writeContents(webInfDirectory, "web.xml", webXMLContents);
        }
    }

    @Override
    public void adaptMavenModel(Model pomFile, JessieModel model, boolean mainProject) {
        String thorntailVersion = "";
        switch (model.getSpecification().getMicroProfileVersion()) {

            case NONE:
                break;
            case MP30:
                thorntailVersion = "2.5.0.Final";
                break;
            case MP22:
                thorntailVersion = "2.4.0.Final";
                break;
            case MP21:
                thorntailVersion = "2.3.0.Final";
                break;
            case MP20:
                break;
            case MP14:
                break;
            case MP13:
                thorntailVersion = "2.1.0.Final";
                break;
            case MP12:
                thorntailVersion = "2.0.0.Final";
                break;
            default:
        }
        pomFile.addProperty("version.thorntail", thorntailVersion);

    }
}
