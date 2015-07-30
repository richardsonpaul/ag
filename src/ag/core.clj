(ns ag.core)

(defmacro game [name]
  `(do
     (ns ~name (:use ag.core))
     (def ~'current (atom nil))
     (def ~'pages (atom {}))

     (defn ~'what-happened? []
       (println (-> @~'pages (@~'current) (get 0)))
       '~'.)))

(defmacro choose [choice]
  `(let [previous# (-> @~'current name symbol)]
     (try
       (reset! ~'current ((get-in @~'pages [@~'current 1]) ~(keyword choice)))
       (catch IllegalArgumentException ~'_ (println "That is not a valid choice!")))
     (~'what-happened?)
     previous#))

(defmacro page
  "Makes a page in your adventure book.
  (page *name* *description* *choices*)"
  [name desc & cases]
  `(swap! ~'pages assoc ~(keyword name)
     [~desc
      #(case % ~@(map keyword cases))]))

(defmacro start [s]
  `(do
     (defn ~'start-over []
       (reset! ~'current ~(keyword s))
       (~'what-happened?))
     (~'start-over)))

;; go to a page
