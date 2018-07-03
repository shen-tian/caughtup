(ns caughtup.core
  (:require
   [reagent.core :as reagent]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(def activies
  [{:verb    "seen"
    :objects ["Instagram posts"
              "dank memes"
              "Trump news"
              "all /r/aww top posts"]}
   {:verb    "watched"
    :objects ["episodes of Stranger Things"
              "World Cup matches"
              "Marvel movies"
              "Beyonce visual albums"
              "Masterchef Australia"]}
   {:verb    "listened to"
    :objects ["Björk albums"
              "Ira Glass podcasts"
              "Lofi Wu Tang Clan covers"
              "Guilty Feminist episodes"]}
   {:verb    "read"
    :objects ["Ed Young articles"
              "Erza Klein interviews"
              "Westworld thinkpieces"
              "McSweeney's listicles"]}])

(def periods
  [{:unit "hours"
    :max 24}
   {:unit "years"
    :max 20}
   {:unit "milliseconds"
    :max 1000}
   {:unit "seconds"
    :max 60}
   {:unit "minutes"
    :max 60}
   {:unit "days"
    :max 30}
   {:unit "months"
    :max 12}])

(defn activity []
  (let [{:keys [verb objects]} (rand-nth activies)]
    (str verb " all new " (rand-nth objects))))

(defn period []
  (let [{:keys [unit max]} (rand-nth periods)]
    (str (+ 2 (rand-int (- max 2))) " " unit)))

(defonce app-state
  (reagent/atom {:activity (activity)
                 :period (period)}))

(defn rotate []
  (reset! app-state {:activity (activity)
                     :period   (period)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(def font-stack "-apple-system, BlinkMacSystemFont, Roboto, Oxygen, Ubuntu, Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', Arial, sans-serif")



(defn page [ratom]
  [:div {:style {:display        :flex
                 :flex-direction :column
                 :align-items    :center
                 :font-family    font-stack}}
   [:img {:style {:margin-top "30vh"
                  :width      "80%"
                  :max-width  "400px"}
          :src   "/img/check.png"}]
   [:h3 {:style {:font-size  "20px"
                 :margin     "30px"
                 :text-align :center}}
    "You've " (:activity @ratom) " from the past " (:period @ratom)]
   [:p {:style {:margin "20vh 5px"
                :color "#ccc"}}
    "You are free to do something else"]])



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload)
  (.setInterval js/window rotate 5000))
