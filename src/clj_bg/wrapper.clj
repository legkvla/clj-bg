(ns clj-bg.wrapper
  (:require
    [clojure.tools.logging :refer [error]]
    [metrics.core :refer [default-registry]]
    [metrics.timers :refer [timer] :as tmr]))

(defn wrap-handler [handler-id handler-fn data]
  (let [op-ctx (tmr/start (timer default-registry (name handler-id)))]
    (try
      (handler-fn data)
      (catch Exception ex
        (error ex
          (str "Error processing " handler-id))
        (send-alert (make-alert ex handler-id data))
        (throw ex))
      (finally (tmr/stop op-ctx)))))

(defn wrap-user-job [job-id job-fn user]
  (let [op-ctx (tmr/start (timer default-registry (name job-id)))]
    (try
      (job-fn user)
      (catch Exception ex
        (error ex
          (str "Error processing " job-id " for user: " user))
        (send-alert (make-alert ex job-id user {}))
        nil)
      (finally (tmr/stop op-ctx)))))

(defn wrap-user-task [task-id task-fn user data]
  (let [op-ctx (tmr/start (timer default-registry (name task-id)))]
    (try
      (task-fn user data)
      (catch Exception ex
        (error ex
          (str
            "Error processing " task-id
            " for user: " user
            " with data: " data))
        (send-alert (make-alert ex task-id user data))
        nil)
      (finally (tmr/stop op-ctx)))))

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
