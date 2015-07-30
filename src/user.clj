(ns user
  (:use ag.core))

(defmacro play [game]
  `(do
     (require '~game)
     (in-ns '~game)
     (~'start-over)))
