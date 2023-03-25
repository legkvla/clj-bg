(ns clj-bg.wrapper
  (:require
    [clojure.tools.logging :refer [error]]
    [metrics.core :refer [default-registry]]
    [metrics.timers :refer [timer] :as tmr]))

(defn wrap-task [task-id data fn:task]
  (let [op-ctx (tmr/start (timer default-registry (name task-id)))]
    (try
      (fn:task data)
      (catch Exception ex
        (error ex
          (str "Error processing " task-id " with data: " data))
        (send-alert (make-alert ex task-id nil data))
        nil)
      (finally (tmr/stop op-ctx)))))

(defn wrap-job [job-id job-fn]
  (let [op-ctx (tmr/start (timer default-registry (name job-id)))]
    (try
      (job-fn)
      (catch Exception ex
        (error ex (str "Error processing " job-id))
        (send-alert (make-alert ex job-id nil {}))
        nil)
      (finally (tmr/stop op-ctx)))))
