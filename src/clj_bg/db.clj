(ns clj-bg.db)

(defprotocol DbFacade
  (find-all [this coll where-fields sort-fields])
  (find-by-id [this coll id])
  (find-first [this coll field value])
  (find-and-modify [this coll doc field expected-value new-value])
  (save [this coll rec])
  (update-all [this coll where-fields update-fields]))

(defn check-filter [v where-fields])

;Simplified default backend for testing
(deftype InMemoryDb [a:db]
  DbFacade
  (find-all [this coll where-fields sort-fields]
    (->> @a:db vals
      (filter
        (fn [v]))))
  (find-by-id [this coll id]
    (get @a:db id))
  (find-first [this coll field value]
    (->> @a:db vals
      (filter (fn [v] (-> v (get field) (= value))))
      first))
  (find-and-modify [this coll doc field expected-value new-value]
    (swap! a:db))

  (save [this coll rec]
    (swap! a:db assoc (:id rec) rec))
  (update-all [this coll where-fields update-fields]
    (swap! a:db)))

(def a:db-backend (atom (InMemoryDb. (atom {}))))

(defn init-db-backend [db-backend]
  (swap! a:db-backend db-backend))

(defn find-all [coll where-fields sort-fields]
  ())

(defn find-by-id [coll id])

(defn find-first [coll field value])

(defn find-and-modify [coll doc field expected-value new-value])

(defn save [coll rec])

(defn update-all [coll where-fields update-fields])
