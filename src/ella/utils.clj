(ns ella.utils
  (:import (org.elasticsearch.common.xcontent XContentFactory ToXContent XContentBuilder)))


(defn execget [x] (.actionGet (.execute x)))
(defn clusterclient [x] (.cluster (.admin @(:client x))))
(defn indexclient [x] (.indices (.admin @(:client x))))
(defn client [x]  @(:client x))
(defn xtostr [ xcontent] (let [e (.prettyPrint ^XContentBuilder (XContentFactory/jsonBuilder))]
                                      (do
                                        (.startObject e)
                                        (.toXContent xcontent e nil)
                                        (.endObject e)
                                        (.string e))))
