package dk.alexandra.fresco.tools.ot.otextension;

import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.util.Drbg;
import dk.alexandra.fresco.framework.util.StrictBitVector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Protocol class for the party acting as the sender in an correlated OT with errors extension.
 */
public class CoteSender extends CoteShared {
  private final OtExtensionResourcePool resources;
  private final Network network;
  // The prgs based on the seeds learned from OT
  private final List<Drbg> prgs;
  // The random messages choices for the random seed OTs
  private StrictBitVector otChoices;

  /**
   * Construct a sending party for an instance of the correlated OT protocol.
   *
   * @param resources The common resource pool needed for OT extension
   * @param network The network interface. Must not be null and must be initialized.
   */
  public CoteSender(OtExtensionResourcePool resources, Network network) {
    super(resources.getInstanceId());
    this.prgs = new ArrayList<>(resources.getComputationalSecurityParameter());
    for (StrictBitVector message : resources.getSeedOts().getLearnedMessages()) {
      // Initialize the PRGs with the random messages
      prgs.add(initPrg(message));
    }
    otChoices = resources.getSeedOts().getChoices();
    this.resources = resources;
    this.network = network;
  }

  /**
   * Returns a clone of the random bit choices used for OT.
   *
   * @return A clone of the OT choices
   */
  public StrictBitVector getDelta() {
    // Return a new copy to avoid issues in case the caller modifies the bit
    // vector
    return new StrictBitVector(otChoices.toByteArray());
  }

  /**
   * Constructs a new batch of correlated OTs with errors.
   *
   * @param size Amount of OTs to construct
   */
  public List<StrictBitVector> extend(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("The amount of OTs must be a positive integer");
    }
    if (size % Byte.SIZE != 0) {
      throw new IllegalArgumentException(
          "The amount of OTs must be a positive integer divisible by 8");
    }
    int bytesNeeded = size / Byte.SIZE;
    final List<StrictBitVector> tlist = prgs.parallelStream()
        .limit(resources.getComputationalSecurityParameter())
        .map(drbg -> {
          byte[] bytes = new byte[bytesNeeded];
          drbg.nextBytes(bytes);
          return bytes;
        })
        .map(StrictBitVector::new)
        .collect(Collectors.toList());
    final List<StrictBitVector> ulist = receiveList(resources.getComputationalSecurityParameter());
    IntStream.range(0, resources.getComputationalSecurityParameter()).parallel()
        .filter(i -> otChoices.getBit(i, false))
        .forEach(i -> tlist.get(i).xor(ulist.get(i)));
    return Transpose.transpose(tlist);
  }

  /**
   * Receives a list of StrictBitVectors from the default (0) channel.
   *
   * @param size Amount of elements in vector to receive. All of which must be of equal size.
   * @return The list of received elements, or null in case an error occurred.
   */
  private List<StrictBitVector> receiveList(int size) {
    List<StrictBitVector> list = new ArrayList<>(size);
    byte[] byteBuffer = network.receive(resources.getOtherId());
    int elementLength = byteBuffer.length / size;
    for (int i = 0; i < size; i++) {
      byte[] currentVector = new byte[elementLength];
      System.arraycopy(byteBuffer, i * elementLength, currentVector, 0, elementLength);
      StrictBitVector currentArr = new StrictBitVector(currentVector);
      list.add(currentArr);
    }
    return list;
  }
}
