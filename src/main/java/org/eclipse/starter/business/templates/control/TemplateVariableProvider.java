/*
 * Copyright (c) 2017-2019 Contributors to the Eclipse Foundation
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
 * Contributors:
 *   2018-09-29 - Rudy De Busscher
 *      Initially authored in Atbash Jessie
 */
package org.eclipse.starter.business.templates.control;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.starter.business.model.entity.JessieModel;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class TemplateVariableProvider {

    public Map<String, Object> determineVariables(JessieModel model) {
        Map<String, Object> result = new HashMap<>();

        result.put("java_version", model.getJavaSEVersion().getCode());

        result.put("java_package", model.getGroupId() + '.' + model.getPackageName());
        result.put("maven_groupid", model.getGroupId());
        result.put("maven_artifactid", model.getArtifactId());

        String artifactId = model.getArtifactId().replaceAll("\\.", "");
        result.put("artifact", StringUtils.capitalize(artifactId));
        result.put("application", StringUtils.capitalize(artifactId.replaceAll("-", "")));

        result.put("mp_version", model.getMicroProfileVersion().getCode());
        result.put("mp_mvn_version", model.getMicroProfileVersion().getMavenVersion());

        return result;

    }
}