(ns clj-bg.db)

(defn find-all [coll where-fields sort-fields])

(defn find-by-id [coll id])

(defn find-first [coll field value])

(defn find-and-modify [coll {:keys [id] :as doc} field expected-value new-value])

(defn save [coll rec])

(defn update-all [coll where-fields update-fields])
