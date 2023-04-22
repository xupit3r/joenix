#!/usr/bin/env bb

(require 
  '[babashka.http-client :as http]
  '[cheshire.core :as json])

;; this will retrieve weather data from Open Meteo

(def API_BASE "https://api.open-meteo.com/v1/forecast?latitude=40.43&longitude=-79.93&hourly=temperature_2m,precipitation&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&forecast_days=1")

(defn relevant [data] 
  {:temperature (int (Math/floor (:temperature (:current_weather data))))
   :units (:temperature_2m (:hourly_units data))
   :icon (:weathercode (:current_weather data))
})

(defn decode [s] (json/parse-string s true))
(defn encode [s] (json/generate-string s))

(->
  API_BASE 
  http/get 
  :body
  decode
  relevant
  encode
  println)