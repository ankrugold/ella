(ns ella.indexing
  (:use
    [ella.client] [ella.utils] [ella.admin])
  (:import (org.elasticsearch.client AdminClient Client ClusterAdminClient IndicesAdminClient)
           (ella.client ESClient)
           (org.elasticsearch.action.index IndexRequestBuilder)
           (org.elasticsearch.common.xcontent XContentFactory)
           (org.elasticsearch.action.admin.indices.mapping.delete DeleteMappingRequestBuilder)))


(defprotocol IndexProto
  (indexdat [this,index,type,id,data])
  (deletedat [this,index,type,id])
  (putmapping [this,index,type,source])
  (createindex [this,index])
  (deleteindex [this,index])
  (deletemapping [this,index,mapping])
  )

(extend-type ESClient
  IndexProto
  (indexdat [this,index,type,id,data]
    (let [res (execget (.setSource ^IndexRequestBuilder (.prepareIndex ^Client (client this) index type id)
                                   (doto (XContentFactory/jsonBuilder)
                                     (.startObject)
                                     (#(doseq [kv data] (.field % (key kv) (val kv))))
                                     (.endObject))))]

      (do (print (.isCreated res))
                (println (.getType res))
                (println (.getIndex res))
                (println (.getVersion res))
                (println (.getId res))))
    )

  (deletedat [this,index,type,id] (.prepareDelete (client this) index type id))

  (putmapping [this,index,type,source]
    (.isAcknowledged (execget (.setSource (.setType (.preparePutMapping ^Client (indexclient this) (into-array index)) type) source))))

  (createindex [this,index]
    (.isAcknowledged (execget (.prepareCreate (indexclient this)  index))))

  (deleteindex [this,index]
    (.isAcknowledged (execget (.prepareDelete (indexclient this) (into-array index)))))

  (deletemapping [this,index,mapping]
    (.isAcknowledged (execget (.setType ^DeleteMappingRequestBuilder (.prepareDeleteMapping (indexclient this) (into-array index)) (into-array mapping))))
    )

  )