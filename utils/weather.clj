#!/usr/bin/env bb

(require 
  '[babashka.http-client :as http]
  '[cheshire.core :as json])

;; this will retrieve weather data from Open Meteo

(def API_BASE "https://api.open-meteo.com/v1/forecast?latitude=40.43&longitude=-79.93&hourly=temperature_2m,precipitation&current_weather=true&temperature_unit=fahrenheit&windspeed_unit=mph&precipitation_unit=inch&forecast_days=1")

(def WMO {
  0   {:description "Clear sky" :icon ""}
  1   {:description "Mainly clear" :icon ""}
  2   {:description "Partly cloudy" :icon ""}
  3   {:description "Overcast" :icon ""}
  45  {:description "Fog" :icon ""}
  48 	{:description "Fog" :icon ""}
  51  {:description "Drizzle: light" :icon ""}
  53  {:description "Drizzle: moderate" :icon ""}
  55 	{:description "Drizzle: dense intensity" :icon ""}
  56  {:description "Freezing drizzle: light" :icon ""}
  57 	{:description "Freezing drizzle: dense intensity" :icon ""}
  61 	{:description "Rain: slight" :icon ""}
  63 	{:description "Rain: moderate" :icon ""}
  65 	{:description "Rain: heavy intensity" :icon ""}
  66 	{:description "Freezing rain: light" :icon ""}
  67 	{:description "Freezing rain: heavy intensity" :icon ""}
  71  {:description "Snow fall: slight" :icon ""}
  73  {:description "Snow fall: moderate" :icon ""}
  75 	{:description "Snow fall: heavy intensity" :icon ""}
  77 	{:description "Snow grains" :icon ""}
  80 	{:description "Rain showers: slight" :icon ""}
  81 	{:description "Rain showers: moderate" :icon ""}
  82 	{:description "Rain showers: violent" :icon ""}
  85 	{:description "Snow showers: slight" :icon ""}
  86 	{:description "Snow showers: heeavy" :icon ""}
  95  {:description "* Thunderstorm" :icon ""}
  96  {:description "* Thunderstorm with slight hail" :icon ""}
  99  {:description "* Thunderstorm with heavy hail" :icon ""}
})


(defn pull-temperature [data]
  (int (Math/floor (:temperature (:current_weather data)))))

(defn pull-units [data]
  (:temperature_2m (:hourly_units data)))

(defn pull-conditions [data]
  (get WMO 
       (:weathercode (:current_weather data)) 
       {:description "Conditions unavailable." :icon ""}))

(defn relevant [data] 
  {:temperature (pull-temperature data)
   :units (pull-units data)
   :conditions (pull-conditions data)})

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
