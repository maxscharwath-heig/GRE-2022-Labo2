package graph.core.impl;

import graph.core.Edge;
import graph.core.EdgeFactory;
import graph.core.Vertex;
import graph.core.VertexFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Digraph with generic vertices and edges
 *
 * Graph uses underlying unmodifiable collections but isn't immutable though
 * since this class has no control over the used vertex and edge types
 *
 * @author Henrik Akesson, Thibaud Franchetti
 */
public final class Digraph<V extends Vertex, E extends Edge<V>> {

  /**
   * Digraph builder
   * @param <V> Vertex type
   * @param <E> Edge type
   * @param <VData> Additional data type delegated to a {@link VertexFactory}
   * @param <EData> Additional data type delegated to an {@link EdgeFactory}
   */
  public static class Builder<V extends Vertex, E extends Edge<V>, VData, EData> {
    /** List of vertices */
    private final ArrayList<V> vertices;

    /** List of edges */
    private final List<List<E>> edges;

    /** Factory for building vertices */
    private final VertexFactory<V, VData> vertexFactory;

    /** Factory for building edges */
    private final EdgeFactory<V, E, EData> edgeFactory;

    /**
     * @param vertexFactory Factory for building vertices
     * @param edgeFactory Factory for building edges
     * @param nVertices Number of vertices in the graph
     */
    public Builder(final VertexFactory<V, VData> vertexFactory,
                   final EdgeFactory<V, E, EData> edgeFactory,
                   final int nVertices) {
      this.vertexFactory = vertexFactory;
      this.edgeFactory = edgeFactory;

      if (nVertices <= 0)
        throw new IllegalArgumentException("nVertices cannot be <= 0");

      this.vertices = new ArrayList<>(nVertices);
      this.edges = new ArrayList<>(nVertices);

      for (int i = 0; i < nVertices; ++i) {
        this.vertices.add(null);
        edges.add(new LinkedList<>());
      }
    }

    /**
     * Builds a vertex and adds it to the graph
     * @param id Vertex id
     * @throws IllegalArgumentException if id invalid
     */
    public void addVertex(final int id) {
      addVertex(id, null);
    }

    /**
     * Builds a vertex and adds it to the graph
     * @param id Vertex id
     * @param additionalData Additional data delegated to the {@link VertexFactory}
     * @throws IllegalArgumentException if id invalid
     */
    public void addVertex(final int id, final VData additionalData) {
      assertValidId(id);
      vertices.set(id, vertexFactory.makeVertex(id, additionalData));
    }

    /**
     * Builds an edge and adds it to the graph
     * @param from Id of origin vertex
     * @param to Id of destination vertex
     * @throws IllegalArgumentException if vertices of id from or to aren't set yet
     * @throws IllegalArgumentException if from or to invalid
     */
    public void addEdge(final int from, final int to) {
      addEdge(from, to, null);
    }

    /**
     * Builds an edge and adds it to the graph
     * @param from Id of origin vertex
     * @param to Id of destination vertex
     * @param additionalData Additional data delegated to the {@link EdgeFactory}
     * @throws IllegalArgumentException if vertices of id from or to aren't set yet
     * @throws IllegalArgumentException if from or to invalid
     */
    public void addEdge(final int from, final int to, final EData additionalData) {
      assertValidId(from);
      assertValidId(to);
      if (vertices.get(from) == null || vertices.get(to) == null)
        throw new IllegalArgumentException("Vertex.ices not set");

      edges.get(from).add(edgeFactory.makeEdge(vertices.get(from), vertices.get(to), additionalData));
    }

    /**
     * Builds the graph
     * @return a new graph
     */
    public Digraph<V, E> build() {
      if (Stream.of(vertices).anyMatch(Objects::isNull))
        throw new RuntimeException("Cannot build graph, missing vertices...");

      return new Digraph<>(
        Collections.unmodifiableList(vertices),
        edges.stream().map(Collections::unmodifiableList).collect(Collectors.toUnmodifiableList())
      );
    }

    /**
     * Asserts the given id is valid (between 0 and number of vertices - 1)
     * @param id an id
     */
    private void assertValidId(final int id) {
      if (id < 0 || id >= vertices.size())
        throw new IllegalArgumentException("Vertex id must be between " + 0 + " and " + (vertices.size() - 1));
    }
  }

  /** Vertices **/
  private final List<V> vertices;
  /** Successor lists. */
  private final List<List<E>> successorLists;

  /**
   * Constructs a new directed graph with given number of vertices.
   *
   * @param vertices Number of vertices of the graph.
   */
  private Digraph(final List<V> vertices, final List<List<E>> successorLists) {
    this.vertices = vertices;
    this.successorLists = successorLists;
  }

  /** @return Number of vertices. */
  public int getNVertices() {
    return vertices.size();
  }


  /**
   * @param vertex Vertex index.
   *
   * @return Successor list of given vertex.
   * @throws ArrayIndexOutOfBoundsException
   */
  public List<E> getSuccessorList(final int vertex) {
    return successorLists.get(vertex);
  }

  /**
   * @return All vertices
   */
  public List<V> getVertices() {
    return vertices;
  }

}
