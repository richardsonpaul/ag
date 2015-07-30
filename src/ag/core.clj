(ns ag.core)

(defmacro game [name]
  `(do
     (ns ~name (:use ag.core ~'user))
     (def ~'current (atom nil))
     (def ~'pages (atom {}))

     (defn ~'what-happened? []
       (println (-> @~'pages (@~'current) (get 0)))
       (-> ~'current deref name symbol))))

(defmacro choose [choice]
  `(try
     (goto ((get-in @~'pages [@~'current 1]) ~(keyword choice)))
     (catch IllegalArgumentException ~'_ (println "That is not a valid choice!"))))

(defmacro page
  "Makes a page in your adventure book.
  (page *name* *description* *choices*)"
  [name desc & cases]
  `(swap! ~'pages assoc ~(keyword name)
     [~desc
      #(case % ~@(map keyword cases))]))

(defmacro start [s]
  `(defn ~'start-over []
     (go-to ~s)))

(defmacro goto [page]
  `(do
     (reset! ~'current ~page)
     (~'what-happened?)))

(defmacro go-to [page]
  `(goto (keyword '~page)))
