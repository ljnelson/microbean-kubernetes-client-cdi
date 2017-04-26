/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2017 MicroBean.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.kubernetes.client.cdi;

import java.io.Closeable;
import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.BeforeDestroyed;
import javax.enterprise.context.Initialized;

import javax.enterprise.event.Observes;

import javax.enterprise.inject.Produces;

import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.Watcher.Action;

import io.fabric8.kubernetes.client.dsl.PodResource;

import io.fabric8.kubernetes.client.utils.HttpClientUtils;

import okhttp3.OkHttpClient;

import org.microbean.kubernetes.client.cdi.annotation.Added;

@ApplicationScoped
class Producers {

  private volatile Closeable watch;

  private volatile KubernetesClientException exceptionFromEvents;

  private Producers() {
    super();
  }

  @Produces
  private static final OkHttpClient produceOkHttpClient(final Config config) {
    return HttpClientUtils.createHttpClient(config);
  }

  @Produces
  private static final Config produceConfig() {
    return new ConfigBuilder().build();
  }
  
  @Produces
  private static final KubernetesClient produceKubernetesClient(final OkHttpClient httpClient, final Config config) {

    /*
      Note that the kubernetes-client project is entirely
      undocumented, and the documentation that happens to exist is
      incorrect.

      To create a
      self-configuring-from-System-properties-and-environment-variables
      KubernetesClient, you just create a new DefaultKubernetesClient.

      You can use an io.fabric8.kubernetes.client.ConfigBuilder, which
      appears to be generated by some process as there is no source
      code for it, and call these builder methods (I figured out what
      they were by running javap).  In general, the "write" property
      is withXYZ(), and the "read" property is the concatenation of
      "get" or "is" and XYZ.

      public ClientBuilder withTrustCerts(boolean);
      public ClientBuilder withMasterUrl(java.lang.String);
      public ClientBuilder withApiVersion(java.lang.String);
      public ClientBuilder withNamespace(java.lang.String);
      public ClientBuilder withCaCertFile(java.lang.String);
      public ClientBuilder withCaCertData(java.lang.String);
      public ClientBuilder withClientCertFile(java.lang.String);
      public ClientBuilder withClientCertData(java.lang.String);
      public ClientBuilder withClientKeyFile(java.lang.String);
      public ClientBuilder withClientKeyData(java.lang.String);
      public ClientBuilder withClientKeyAlgo(java.lang.String);
      public ClientBuilder withClientKeyPassphrase(java.lang.String);
      public ClientBuilder withUsername(java.lang.String);
      public ClientBuilder withPassword(java.lang.String);
      public ClientBuilder withOauthToken(java.lang.String);
      public ClientBuilder withWatchReconnectInterval(int);
      public ClientBuilder withWatchReconnectLimit(int);
      public ClientBuilder withConnectionTimeout(int);
      public ClientBuilder withRequestTimeout(int);
      public ClientBuilder withRollingTimeout(long);
      public ClientBuilder withScaleTimeout(long);
      public ClientBuilder withLoggingInterval(int);
      public ClientBuilder withWebsocketTimeout(long);
      public ClientBuilder withWebsocketPingInterval(long);
      public ClientBuilder withHttpProxy(java.lang.String);
      public ClientBuilder withHttpsProxy(java.lang.String);
      public ClientBuilder withProxyUsername(java.lang.String);
      public ClientBuilder withProxyPassword(java.lang.String);
      public ClientBuilder withNoProxy(java.lang.String...);
      public ClientBuilder addToNoProxy(java.lang.String...);
      public ClientBuilder removeFromNoProxy(java.lang.String...);
      public ClientBuilder withUserAgent(java.lang.String);
      public ClientBuilder withTlsVersions(okhttp3.TlsVersion...);
      public ClientBuilder addToTlsVersions(okhttp3.TlsVersion...);
      public ClientBuilder removeFromTlsVersions(okhttp3.TlsVersion...);
      public ClientBuilder addToErrorMessages(java.lang.Integer, java.lang.String);
      public ClientBuilder addToErrorMessages(java.util.Map<java.lang.Integer, java.lang.String>);
      public ClientBuilder removeFromErrorMessages(java.lang.Integer);
      public ClientBuilder removeFromErrorMessages(java.util.Map<java.lang.Integer, java.lang.String>);
      public ClientBuilder withErrorMessages(java.util.Map<java.lang.Integer, java.lang.String>);
    */
    
    return new DefaultKubernetesClient(httpClient, config);
  }

 
  
}