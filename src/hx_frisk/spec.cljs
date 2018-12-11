(ns datafrisk.spec
  (:require [hx-frisk.view :refer [Root]]
            [hx.react :as hx]
            [hx.react.hooks :refer [<-state]))

(defn spec-problem->metadata-path [{:keys [in path pred val via]}]
  [in {:error (str "(not "
                     (clojure.string/replace (str pred) "cljs.core/" "")
                     ")")}])

(defn <-frisk-errors [id errors]
  {:data (:cljs.spec.alpha/value errors)
   :state (<-state {:data-frisk {id {:metadata-paths (-> (into {} (map spec-problem->metadata-path (:cljs.spec.alpha/problems errors)))
                                                        (update [] assoc :expanded? true))}}})})

(hx/defnc SpecView [{:keys [errors]}]
  (let [mangled (<-frisk-errors "spec-errors" errors)]
    [Root {:data (:data mangled) :id "spec-errors" :state-atom (:state mangled)}]))

(hx/defnc SpecTitleView [{:keys [errors title] :as args}]
  [:div {:style {:background-color "white"
                 :padding "10px"}}
   (if title
     [:div {:style (:style title {})} (:text title)]
     [:div {}
      [:span {:style {:font-weight "700" :color "red"}} "Did not comply with spec "]
      [:span {:style {}} (str (:cljs.spec.alpha/spec errors))]])
   [SpecView args]])
