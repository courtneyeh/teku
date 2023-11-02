/*
 * Copyright Consensys Software Inc., 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.networking.eth2.rpc.beaconchain.methods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.pegasys.teku.kzg.KZG;
import tech.pegasys.teku.kzg.KZGException;
import tech.pegasys.teku.spec.Spec;
import tech.pegasys.teku.spec.datastructures.blobs.versions.deneb.BlobSidecar;

public class AbstractBlobSidecarsValidatingProxy {

  private static final Logger LOG = LogManager.getLogger();

  protected final Spec spec;
  protected final KZG kzg;

  public AbstractBlobSidecarsValidatingProxy(final Spec spec, final KZG kzg) {
    this.spec = spec;
    this.kzg = kzg;
  }

  protected boolean verifyBlobSidecar(final BlobSidecar blobSidecar) {
    try {
      return spec.atSlot(blobSidecar.getSlot()).miscHelpers().verifyBlobKzgProof(kzg, blobSidecar);
    } catch (final KZGException ex) {
      LOG.debug("KZG verification failed for BlobSidecar {}", blobSidecar.toLogString());
      return false;
    }
  }
}