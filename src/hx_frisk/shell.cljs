(ns hx-frisk.shell
  (:require [hx-frisk.view :as view]
            [hx.react :as hx]
            [hx.react.hooks :refer [<-state]]))

(def styles
  {:shell {:backgroundColor "#FAFAFA"
           :fontFamily "Consolas,Monaco,Courier New,monospace"
           :fontSize "12px"
           :z-index 9999}
   :shell-visible-button {:backgroundColor "#4EE24E"}})

(hx/defnc DataFriskShellVisibleButton [{:keys [visible toggle-visible-fn]}]
  [:button {:onClick toggle-visible-fn
            :style (merge {:border 0
                           :cursor "pointer"
                           :font "inherit"
                           :padding "8px 12px"
                           :position "fixed"
                           :right 0
                           :width "80px"
                           :text-align "center"}
                          (:shell-visible-button styles)
                          (when-not visible {:bottom 0}))}
   (if visible "Hide" "Data frisk")])

(hx/defnc DataFriskShellView [{:keys [shell-state data]}]
  (let [visible? (:shell-visible? @shell-state)]
    [:div {:style (merge {:position "fixed"
                          :right 0
                          :bottom 0
                          :width "100%"
                          :height "50%"
                          :max-height (if visible? "50%" 0)
                          :transition "all 0.3s ease-out"
                          :padding 0}
                         (:shell styles))}
     [DataFriskShellVisibleButton {:visible visible?
                                   :toggle-visible-fn
                                   (fn [_] (swap! shell-state assoc :shell-visible? (not visible?))) }]
     [:div {:style {:padding "10px"
                    :height "100%"
                    :box-sizing "border-box"
                    :overflow-y "scroll"}}
      (map-indexed (fn [id x]
                     [view/Root {:data x
                                 :id id
                                 :state-atom shell-state
                                 :key id}]) data)]]))

(hx/defnc DataFriskShell [{:keys [data]}]
  (let [expand-by-default (reduce #(assoc-in %1 [:data-frisk %2 :expanded-paths] #{[]}) {} (range (count data)))
        shell-state (<-state expand-by-default)]
    [DataFriskShellView {:shell-state shell-state
                         :data data}]))
