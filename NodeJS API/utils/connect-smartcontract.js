const Web3 = require("web3");
const path = require("path");
const fs = require("fs");
const {
    rejects
} = require("assert");

// Set the Contract
const supplychain = fs.readFileSync(
    path.join(__dirname, "..", "build", "contracts", "SupplyChain.json")
);
const abi = JSON.parse(supplychain).abi;
const contract_Address = "0x6c3560C2f46C35EeaE8f3f791839C79D593971ac";
let contract;

let accounts;
const init = () => {
    let web3 = new Web3(
        new Web3.providers.HttpProvider("http://localhost:7545")
    );
    web3.eth.net
        .isListening()
        .then(() => console.log("web3 is connected"))
        .catch((e) => console.log("Wow. Something went wrong + ", e));

    web3.eth.getAccounts().then((_accounts) => {
        accounts = _accounts;
        web3.eth.getBalance(accounts[0]).then((balance) => {
            console.log("Balance of account 1: ", balance / 10 ** 18);
        });
    });

    contract = new web3.eth.Contract(abi, contract_Address);
};

const minoterieSet = (productId, temp1, hum1) => {
    return new Promise((resolve, reject) => {

        const res = contract.methods
            .minoterieSet(productId, temp1, hum1)
            .send({
                from: accounts[2],
                gas: 1000000
            })
        resolve(res)
        reject("minoterieSet: Error")
    })



};

const grossisteSet = (productId, temp2, hum2) => {

    return new Promise((resolve, reject) => {
        const res = contract.methods
            .grossisteSet(productId, temp2, hum2)
            .send({
                from: accounts[3],
                gas: 1000000
            })

        resolve(res)
        reject("grossisteSet: Error")
    })


};

const boulangerieSet = (productId) => {
    return new Promise((resolve, reject) => {
        const res = contract.methods
            .boulangerieSet(productId)
            .send({
                from: accounts[4],
                gas: 1000000
            })
        resolve(res)
        reject("boulangerieSet: Error")
    })
};


exports.init = init;
exports.minoterieSet = minoterieSet;
exports.grossisteSet = grossisteSet;
exports.boulangerieSet = boulangerieSet;