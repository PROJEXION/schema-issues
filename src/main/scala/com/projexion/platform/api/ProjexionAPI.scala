package com.projexion.platform.api

import caliban.*
import caliban.wrappers.ApolloTracing.apolloTracing
import caliban.wrappers.IncrementalDelivery
import caliban.wrappers.Wrappers.*
import com.projexion.platform.api.graphql.api.Operations.{Mutation, Query}
import zio.*

import scala.language.postfixOps

object ProjexionAPI {
  def makeApi(asd: String): GraphQL[Unit] = {
    val value: RootResolver[Query, Mutation, Unit] = RootResolver(
      queryResolver = Query(
        null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null
      ),
      mutationResolver =
        Mutation(null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null)
    )
    graphQL(
      value
    ) @@
      maxFields(300) @@ // query analyzer that limit query fields
      maxDepth(30) @@ // query analyzer that limit query depth
      timeout(60 seconds) @@ // wrapper that fails slow queries
      printSlowQueries(500 millis) @@ // wrapper that logs slow queries
      printErrors @@ // wrapper that logs errors
      apolloTracing() @@ // wrapper for https://github.com/apollographql/apollo-tracing
      IncrementalDelivery.defer // wrapper that enables @defer directive support
  }

  val layer: ZLayer[String, Nothing, GraphQL[Unit]] =
    ZLayer(ZIO.serviceWith[String](makeApi))
}
