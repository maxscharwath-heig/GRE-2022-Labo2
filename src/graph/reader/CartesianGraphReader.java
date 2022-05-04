package graph.reader;

import graph.core.Edge;
import graph.core.EdgeFactory;
import graph.core.Vertex;
import graph.core.VertexFactory;
import graph.core.impl.Digraph;
import graph.data.CartesianVertexData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Reads an undirected graph (but uses an underlying digraph) which each vertex
 * owns two coordinates (x, y)
 *
 * @author Henrik Akesson, Thibaud Franchetti
 * @see CartesianVertexData
 */
public final class CartesianGraphReader<V extends Vertex, E extends Edge<V>> {
  /** Number of tokens expected to describe a vertex */
  private static final int N_VERTEX_TOKENS = 3;
  /** Error message used if number of vertices does not match announced number. */
  private static final String ILLEGAL_N_VERTICES = "Illegal number of vertices (%d vertices, expected %d)";
  /** Error message used if a line describing a vertex contains an insufficient number of tokens. */
  private static final String ILLEGAL_N_TOKENS = "Invalid number of tokens to describe a vertex (got %d, expected %d)";
  /** Error message used if an edge is declared with an invalid vertex id. */
  private static final String INVALID_VERTEX_ID = "Invalid vertex id (got %d, should be in [%d, %d])";
  /** Error message used when a vertex is missing in the file */
  private static final String MISSING_VERTEX = "Missing vertex description or bad order (id: %d)";

  /** Final graph */
  private final Digraph<V, E> graph;

  /** Builder used to build the graph */
  private final Digraph.Builder<V, E, CartesianVertexData, Void> graphBuilder;

  /** Number of vertices of the graph */
  private final int nVertices;

  /**
   * Reads the graph from an {@link InputStreamReader}
   * @param vertexFactory A {@link VertexFactory} which should probably support
   *                      {@link CartesianVertexData} as delegated data
   * @param edgeFactory An {@link EdgeFactory}
   * @param inputStreamReader An {@link InputStreamReader}
   * @throws IOException if a read error occurred
   * @throws IllegalArgumentException if file is malformed
   */
  public CartesianGraphReader(final VertexFactory<V, CartesianVertexData> vertexFactory,
                              final EdgeFactory<V, E, Void> edgeFactory,
                              final InputStreamReader inputStreamReader) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      String line = bufferedReader.readLine();
      if (line == null) {
        throw new IllegalArgumentException("File empty");
      }

      this.nVertices = Integer.parseInt(line);
      this.graphBuilder = new Digraph.Builder<>(vertexFactory, edgeFactory, nVertices);

      parseVertices(bufferedReader);
      parseEdges(bufferedReader);

      this.graph = graphBuilder.build();
    }
  }

  /**
   * Reads the graph from a file
   * @param vertexFactory A {@link VertexFactory} which should probably support
   *                      {@link CartesianVertexData} as delegated data
   * @param edgeFactory An {@link EdgeFactory}
   * @param filename A file
   * @throws IOException if a read error occurred
   * @throws IllegalArgumentException if file is malformed
   */
  public CartesianGraphReader(final VertexFactory<V, CartesianVertexData> vertexFactory,
                              final EdgeFactory<V, E, Void> edgeFactory,
                              final String filename) throws IOException {
    this(vertexFactory, edgeFactory, new FileReader(filename));
  }

  /**
   * @return the graph
   */
  public Digraph<V, E> graph() {
    return graph;
  }

  /**
   * Assert a value is between 0 and number of vertices - 1
   * @param value A value
   * @return is between 0 and number of vertices - 1
   */
  private boolean notInRange(final int value) {
    return value >= nVertices || value < 0;
  }

  /**
   * Reads vertices
   * @param bufferedReader A reader
   * @throws IOException if a read error occurred
   * @throws IllegalArgumentException if file is malformed
   */
  private void parseVertices(final BufferedReader bufferedReader) throws IOException {

    for (int v = 0; v < nVertices; ++v) {
      CartesianVertexData data = new CartesianVertexData();
      String line = bufferedReader.readLine();
      if (line == null) {
        throw new IllegalArgumentException(String.format(ILLEGAL_N_VERTICES, v - 1, nVertices));
      }
      String[] tokens = line.split(" ");

      // Extra tokens are ignored
      if (tokens.length < N_VERTEX_TOKENS)
        throw new IllegalArgumentException(String.format(ILLEGAL_N_TOKENS, N_VERTEX_TOKENS, tokens.length));

      final int id = Integer.parseInt(tokens[0]);
      data.x = Integer.parseInt(tokens[1]);
      data.y = Integer.parseInt(tokens[2]);

      if (id != v)
        throw new IllegalArgumentException(String.format(MISSING_VERTEX, v));

      graphBuilder.addVertex(id, data);
    }
  }

  /**
   * Reads edges
   * @param bufferedReader A reader
   * @throws IOException if a read error occurred
   * @throws IllegalArgumentException if file is malformed
   */
  private void parseEdges(final BufferedReader bufferedReader) throws IOException {
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      String[] tokens = line.split(" ");

      int from = -1;
      for (String token : tokens) {
        final int v = Integer.parseInt(token);

        if (notInRange(v))
          throw new IllegalArgumentException(String.format(INVALID_VERTEX_ID, v, 0, nVertices - 1));

        if (from == -1) {
          from = v;
        } else {
          graphBuilder.addEdge(from, v);
        }
      }
    }
  }
}
