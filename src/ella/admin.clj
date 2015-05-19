(ns ella.admin
  (:use
    [ella.client] [ella.utils])
  (:import (org.elasticsearch.client AdminClient Client ClusterAdminClient IndicesAdminClient)
           (ella.client ESClient)))

(defprotocol ClusterAdmin
  (state [this])
  (health [this, indices])
  (nodeinfo [this,nodeids])
  (clusterstats [this])
  (nodestats [this,nodeids])
  (hotthreads [this,nodeids])
  )

(defprotocol IndexAdmin
  (exists [this,indices])
  (stats [this,indices])
  (recovery [this,indices])
  (segments [this,indices])
  (mappings [this,indices])
  (fieldmapings [this,indices])
  (aliases [this])
  (aliasesget [this,aliases])
  (aliasesexist [this,aliases])
  )


(extend-type ESClient
  ClusterAdmin
  (state [this]
    (.getState (execget (.prepareState ^ClusterAdminClient (clusterclient this))))
    )
  (health [this, indices]
    (execget (.prepareHealth ^ClusterAdminClient (clusterclient this) (into-array indices)))
    )
  (nodeinfo [this,nodeids]
    (execget (.prepareNodesInfo ^ClusterAdminClient (clusterclient this) (into-array nodeids)))
    )
  (clusterstats [this]
    (execget (.prepareClusterStats ^ClusterAdminClient (clusterclient this)))
    )
  (nodestats [this,nodeids]
    (execget (.prepareNodesStats ^ClusterAdminClient (clusterclient this) (into-array nodeids)))
    )
  (hotthreads [this,nodeids]
    (map #(.getHotThreads %)
         (.getNodes (execget (.prepareNodesHotThreads ^ClusterAdminClient (clusterclient this) (into-array nodeids)))))
    )

  )

(extend-type ESClient
  IndexAdmin
  (exists [this,indices]
    (.isExists (execget (.prepareExists ^IndicesAdminClient (indexclient this) (into-array indices))))
    )
  (stats [this,indices]
    (execget (.prepareStats ^IndicesAdminClient (indexclient this) (into-array indices)))
    )
  (recovery [this,indices]
    (execget (.prepareRecoveries ^IndicesAdminClient (indexclient this) (into-array indices)))
    )
  (segments [this,indices]
    (execget (.prepareSegments ^IndicesAdminClient (indexclient this) (into-array indices)))
    )
  (mappings [this,indices]
    (execget (.prepareGetMappings ^IndicesAdminClient (indexclient this) (into-array indices)))
    )
  (fieldmappings [this,indices]
    (execget (.prepareGetFieldMappings ^IndicesAdminClient (indexclient this) (into-array indices)))
    )
  (aliases [this]
    (execget (.prepareAliases ^IndicesAdminClient (indexclient this)))
    )
  (aliasesget [this,aliases]
    (execget (.prepareGetAliases ^IndicesAdminClient (indexclient this) (into-array aliases)))
    )
  (aliasesexist [this,aliases]
    (execget (.prepareAliasesExist ^IndicesAdminClient (indexclient this) (into-array aliases)))
    )
  )