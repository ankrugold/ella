(ns ella.client
  (:import

    (org.elasticsearch.common.transport InetSocketTransportAddress)
    (org.elasticsearch.common.settings ImmutableSettings)
    (org.elasticsearch.client.transport TransportClient)
    (org.elasticsearch.client Client AdminClient IndicesAdminClient)))


(defprotocol ESProto
  (init [this])
  (destroy [this]))

(defrecord ESClient [cfg ^Client client]
  ESProto
  (init [_] (reset! client (let [cl (new TransportClient
                                         (.build (doto (ImmutableSettings/settingsBuilder)
                                                   (.put "cluster.name" cfg))))]
                             (.addTransportAddress cl (new InetSocketTransportAddress "localhost" 9300)) cl)))

  ;(init[^String clustername ^String ip] (reset! client (let [cl (new TransportClient
  ;                                                     (.build (doto (ImmutableSettings/settingsBuilder)
  ;                                                               (.put "cluster.name" clustername))))]
  ;                                         (.addTransportddress cl (new InetSocketTransportAddress ip 9300)) cl)))
  (destroy [_] (when @client (.close @client) (reset! client nil))))


