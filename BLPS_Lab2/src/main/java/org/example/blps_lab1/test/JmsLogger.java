///* FIXME Jms не нужен
// * Copyright 2020 Red Hat, Inc, and individual contributors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.example.blps_lab1.test;
//
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JmsLogger {
//
//    static final String LOGGER_QUEUE = "log";
//
//    @JmsListener(destination = LOGGER_QUEUE)
//    public void onMessage(String message) {
//        System.out.println("JMS Logger ---> " + message);
//    }
//}
