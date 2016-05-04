package es.upm.oeg.tools.quality.ldsniffer.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright 2014-2016 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Nandana Mihindukulasooriya
 * @since 1.0.0
 */
public class HttpResponse implements Serializable {

    private final int statusCode;

    private final String reason;

    private final String uri;

    private final String method;

    private final boolean cached;

    private final Date date;

    public HttpResponse(String uri, String method, int statusCode, String reason, Date date, boolean cached) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.uri = uri;
        this.method = method;
        this.date = date;
        this.cached = cached;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public boolean isCached() {
        return cached;
    }

    public Date getDate() {
        return date;
    }
}
