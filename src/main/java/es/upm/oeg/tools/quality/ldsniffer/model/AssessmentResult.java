package es.upm.oeg.tools.quality.ldsniffer.model;

import java.util.Map;

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
public class AssessmentResult {

    private final String subject;

    private long numberOfDistinctURIs;

    private long numberOfDereferenceableURIs;

    private long numberOfDereferenceableSubjects;

    private long numberOfDereferenceablePredicts;

    private long getNumberOfDereferenceableObjects;

    private HttpResponse[] errURIs;

    private HttpResponse[] errSubjects;

    private HttpResponse[] errPredicats;

    private HttpResponse[] errObjects;

    public AssessmentResult(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public long getNumberOfDistinctURIs() {
        return numberOfDistinctURIs;
    }

    public void setNumberOfDistinctURIs(long numberOfDistinctURIs) {
        this.numberOfDistinctURIs = numberOfDistinctURIs;
    }

    public long getNumberOfDereferenceableURIs() {
        return numberOfDereferenceableURIs;
    }

    public void setNumberOfDereferenceableURIs(long numberOfDereferenceableURIs) {
        this.numberOfDereferenceableURIs = numberOfDereferenceableURIs;
    }

    public long getNumberOfDereferenceableSubjects() {
        return numberOfDereferenceableSubjects;
    }

    public void setNumberOfDereferenceableSubjects(long numberOfDereferenceableSubjects) {
        this.numberOfDereferenceableSubjects = numberOfDereferenceableSubjects;
    }

    public long getNumberOfDereferenceablePredicts() {
        return numberOfDereferenceablePredicts;
    }

    public void setNumberOfDereferenceablePredicts(long numberOfDereferenceablePredicts) {
        this.numberOfDereferenceablePredicts = numberOfDereferenceablePredicts;
    }

    public long getGetNumberOfDereferenceableObjects() {
        return getNumberOfDereferenceableObjects;
    }

    public void setGetNumberOfDereferenceableObjects(long getNumberOfDereferenceableObjects) {
        this.getNumberOfDereferenceableObjects = getNumberOfDereferenceableObjects;
    }

    public HttpResponse[] getErrURIs() {
        return errURIs;
    }

    public void setErrURIs(HttpResponse[] errURIs) {
        this.errURIs = errURIs;
    }

    public HttpResponse[] getErrSubjects() {
        return errSubjects;
    }

    public void setErrSubjects(HttpResponse[] errSubjects) {
        this.errSubjects = errSubjects;
    }

    public HttpResponse[] getErrPredicats() {
        return errPredicats;
    }

    public void setErrPredicats(HttpResponse[] errPredicats) {
        this.errPredicats = errPredicats;
    }

    public HttpResponse[] getErrObjects() {
        return errObjects;
    }

    public void setErrObjects(HttpResponse[] errObjects) {
        this.errObjects = errObjects;
    }
}
