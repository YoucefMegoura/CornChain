const SupplyChain = artifacts.require("SupplyChain");

contract("SupplyChain", function() {
  it("should assert true", async function(done) {
    await SupplyChain.deployed();
    assert.isTrue(true);
    done();
  });
});
