(ns clj-bg.db)

(defprotocol DbFacade
  (findAll [this coll where-fields sort-fields])
  (findById [this coll id])
  (findFirst [this coll field value])
  (findAndModify [this coll doc field expected-value new-value])
  (save [this coll rec])
  (updateAll [this coll where-fields update-fields]))

(defn check-filter [v where-fields]
  (apply
    and
    (mapv
      (fn [[field condition]]
        (cond
          (and (map? condition) (= :in (first condition)))

          (and (map? condition) (= :lt (first condition)))

          :else))
          
      where-fields)))

;Simplified default backend for testing
(deftype InMemoryDb [a:db]
  DbFacade
  (findAll [this coll where-fields sort-fields]
    (->> @a:db vals
      (filter
        (fn [v]))))
  (findById [this coll id]
    (get @a:db id))
  (findFirst [this coll field value]
    (->> @a:db vals
      (filter (fn [v] (-> v (get field) (= value))))
      first))
  (findAndModify [this coll doc field expected-value new-value]
    (swap! a:db))

  (save [this coll rec]
    (swap! a:db assoc (:id rec) rec))
  (updateAll [this coll where-fields update-fields]
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
