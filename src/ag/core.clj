(ns ag.core)

(def current (atom nil))

(def pages (atom {}))

(defn what-happened? []
  (-> @pages (@current) (get 0)))

(defmacro choose [choice]
  `(do
     (try
       (reset! current ((get-in @pages [@current 1]) ~(keyword choice)))
       (catch IllegalArgumentException ~'_ (println "That is not a valid choice!")))
     (what-happened?)))

(defmacro page
  "Makes a page in your adventure book.
  (page *name* *description* *choices*)"
  [name desc & cases]
  `(swap! pages assoc ~(keyword name)
     [~desc
      #(case % ~@(map keyword cases))]))

(defmacro start [s]
  `(do
     (defn ~'start-over []
       (reset! current ~(keyword s))
       (what-happened?))
     (start-over)))
