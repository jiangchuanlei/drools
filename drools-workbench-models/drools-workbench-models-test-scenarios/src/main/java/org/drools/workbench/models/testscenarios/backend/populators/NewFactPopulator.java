/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.models.testscenarios.backend.populators;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.drools.workbench.models.testscenarios.shared.FactData;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.soup.project.datamodel.commons.types.TypeResolver;

class NewFactPopulator extends FactPopulatorBase {

    private final Object factObject;

    public NewFactPopulator(
            Map<String, Object> populatedData,
            TypeResolver typeResolver,
            FactData fact) throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {
        super(populatedData,
              typeResolver,
              fact);
        factObject = resolveFactObject();
    }

    protected Object resolveFactObject() throws ClassNotFoundException,
            IllegalAccessException,
            InstantiationException {
        Object factObject = typeResolver.resolveType(getTypeName(typeResolver,
                                                                 fact)).newInstance();
        populatedData.put(fact.getName(),
                          factObject);
        return factObject;
    }

    @Override
    public List<FieldPopulator> getFieldPopulators()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        return getFieldPopulators(factObject);
    }

    @Override
    public void populate(KieSession ksession,
                         Map<String, FactHandle> factHandles) {
        factHandles.put(fact.getName(),
                        ksession.insert(factObject));
    }
}
