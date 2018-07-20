(ns caughtup.core
  (:require
   [reagent.core :as reagent]
   [reanimated.core :as anim]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(def activies
  [{:verb    "seen"
    :objects ["Dwayne Johnson Instagram posts"
              "Distracted Boyfriend memes"
              "Trump news"
              "/r/aww top posts"
              "xkcd comics"
              "art"]}
   {:verb    "watched"
    :objects ["Stranger Things episodes"
              "World Cup matches"
              "Marvel movies"
              "Beyoncé visual albums"
              "Masterchef Australia"
              "Falcon 9 launches"
              "Korean skincare videos"]}
   {:verb    "listened to"
    :objects ["Björk albums"
              "Ira Glass podcasts"
              "Lofi Wu Tang Clan covers"
              "Guilty Feminist episodes"]}
   {:verb    "read"
    :objects ["Ed Young articles"
              "Erza Klein interviews"
              "Westworld thinkpieces"
              "McSweeney's listicles"
              "comments on that Gizmodo article"]}])

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

(defonce opacity
  (reagent/atom 1))

(defn rotate []
  (reset! app-state {:activity (activity)
                     :period   (period)})
  (reset! opacity 1)
  (.setTimeout js/window #(reset! opacity 0)
               6000))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(def font-stack "-apple-system, BlinkMacSystemFont, Roboto, Oxygen, Ubuntu, Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', Arial, sans-serif")


(defn main-text [ratom]
  (let [render-opacity (anim/interpolate-to opacity)]
    (fn [ratom]
      #_(.log js/console "render")
      [:h3 {:style {:font-size   "18px"
                    :opacity     @render-opacity
                    :color       "#aaa"
                    :line-height 1.6
                    :margin      "15px"
                    :text-align  :center}}
       "You've " (:activity @ratom) " from the past " (:period @ratom) "."])))


(defn page [ratom]
  [:div {:style {:display         :flex
                 :flex-direction  :column
                 :align-items     :center
                 :justify-content :space-between
                 :height          "90vh"
                 :font-family     font-stack}}
   [:div ""]
   [:div {:style {:display        :flex
                  :flex-direction :column
                  :align-items    :center}}
    [:img {:style {:width     "80%"
                   :max-width "400px"}
           :src   "/img/check.png"}]
    [:h2 {:style {:font-size "24px"
                  :margin    "15px"}}
     "You're All Caught Up"]
    [main-text ratom]]
   [:p {:style {:margin "20px 5px"
                :color  "#ccc"}}
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
  (.setInterval js/window rotate 7000))
