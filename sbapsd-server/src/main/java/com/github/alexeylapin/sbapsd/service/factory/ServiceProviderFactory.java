/*
 * MIT License
 *
 * Copyright (c) 2023 Alexey Lapin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.alexeylapin.sbapsd.service.factory;

import com.github.alexeylapin.sbapsd.config.def.ServiceProviderDef;
import com.github.alexeylapin.sbapsd.service.ServiceProvider;
import com.github.alexeylapin.sbapsd.service.Validate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instantiates {@link ServiceProvider} based on {@link ServiceProviderDef}
 */
public interface ServiceProviderFactory {

    ServiceProvider create(ServiceProviderDef serviceProviderDef);

    default Map<String, ServiceProvider> createAll(Map<String, ServiceProviderDef> serviceProviderDefs) {
        Validate.notNull(serviceProviderDefs, "serviceProviderDefs must not be null");
        Map<String, ServiceProvider> serviceProviders = new ConcurrentHashMap<>();
        for (Map.Entry<String, ServiceProviderDef> entry : serviceProviderDefs.entrySet()) {
            ServiceProviderDef serviceProviderDef = entry.getValue();
            if (serviceProviderDef.getName() == null) {
                serviceProviderDef.setName(entry.getKey());
            }
            ServiceProvider instanceProvider = create(serviceProviderDef);
            serviceProviders.put(entry.getKey(), instanceProvider);
        }
        return serviceProviders;
    }

}
