const SupplyChain = artifacts.require('SupplyChain.sol')
module.exports = function(_deployer) {
  _deployer.deploy(SupplyChain)
};
