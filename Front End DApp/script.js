if (typeof web3 !== "undefined") {
    web3 = new Web3(web3.currentProvider);
} else {
    web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:7545"));
}
web3.eth.net
    .isListening()
    .then(() => console.log("web3 is connected"))
    .catch((e) => console.log("Wow. Something went wrong + ", e));

let accounts;
web3.eth.getAccounts().then((_accounts) => {
    accounts = _accounts;
    web3.eth.getBalance(accounts[1]).then((balance) => {
        console.log("Balance of account 1: ", balance / 10 ** 18);
    });
});

// Set the Contract
const abi = [
  {
    "inputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "constructor"
  },
  {
    "anonymous": false,
    "inputs": [
      {
        "indexed": false,
        "internalType": "bool",
        "name": "response",
        "type": "bool"
      }
    ],
    "name": "boulangerieSeted",
    "type": "event"
  },
  {
    "anonymous": false,
    "inputs": [
      {
        "indexed": false,
        "internalType": "bool",
        "name": "response",
        "type": "bool"
      }
    ],
    "name": "grossisteSeted",
    "type": "event"
  },
  {
    "anonymous": false,
    "inputs": [
      {
        "indexed": false,
        "internalType": "bool",
        "name": "response",
        "type": "bool"
      }
    ],
    "name": "minoterieSeted",
    "type": "event"
  },
  {
    "anonymous": false,
    "inputs": [
      {
        "indexed": false,
        "internalType": "uint256",
        "name": "index",
        "type": "uint256"
      }
    ],
    "name": "oaicCreated",
    "type": "event"
  },
  {
    "constant": true,
    "inputs": [
      {
        "internalType": "uint256",
        "name": "",
        "type": "uint256"
      }
    ],
    "name": "allProducts",
    "outputs": [
      {
        "internalType": "address",
        "name": "creator",
        "type": "address"
      },
      {
        "internalType": "uint256",
        "name": "productId",
        "type": "uint256"
      },
      {
        "internalType": "string",
        "name": "productName",
        "type": "string"
      },
      {
        "internalType": "uint256",
        "name": "date",
        "type": "uint256"
      },
      {
        "internalType": "enum SupplyChain.StateValue",
        "name": "state",
        "type": "uint8"
      },
      {
        "internalType": "string",
        "name": "temp1",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "hum1",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "temp2",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "hum2",
        "type": "string"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "index",
    "outputs": [
      {
        "internalType": "uint256",
        "name": "",
        "type": "uint256"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "product",
    "outputs": [
      {
        "internalType": "address",
        "name": "creator",
        "type": "address"
      },
      {
        "internalType": "uint256",
        "name": "productId",
        "type": "uint256"
      },
      {
        "internalType": "string",
        "name": "productName",
        "type": "string"
      },
      {
        "internalType": "uint256",
        "name": "date",
        "type": "uint256"
      },
      {
        "internalType": "enum SupplyChain.StateValue",
        "name": "state",
        "type": "uint8"
      },
      {
        "internalType": "string",
        "name": "temp1",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "hum1",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "temp2",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "hum2",
        "type": "string"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "internalType": "string",
        "name": "_productName",
        "type": "string"
      }
    ],
    "name": "newItem",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "internalType": "uint256",
        "name": "_productId",
        "type": "uint256"
      },
      {
        "internalType": "string",
        "name": "_temp1",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "_hum1",
        "type": "string"
      }
    ],
    "name": "minoterieSet",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "internalType": "uint256",
        "name": "_productId",
        "type": "uint256"
      },
      {
        "internalType": "string",
        "name": "_temp2",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "_hum2",
        "type": "string"
      }
    ],
    "name": "grossisteSet",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "internalType": "uint256",
        "name": "_productId",
        "type": "uint256"
      }
    ],
    "name": "boulangerieSet",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "internalType": "uint256",
        "name": "_productId",
        "type": "uint256"
      },
      {
        "internalType": "enum SupplyChain.StateValue",
        "name": "_state",
        "type": "uint8"
      }
    ],
    "name": "setState",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "internalType": "uint256",
        "name": "_productId",
        "type": "uint256"
      }
    ],
    "name": "getProduct",
    "outputs": [
      {
        "internalType": "address",
        "name": "",
        "type": "address"
      },
      {
        "internalType": "uint256",
        "name": "",
        "type": "uint256"
      },
      {
        "internalType": "string",
        "name": "",
        "type": "string"
      },
      {
        "internalType": "uint256",
        "name": "",
        "type": "uint256"
      },
      {
        "internalType": "enum SupplyChain.StateValue",
        "name": "",
        "type": "uint8"
      },
      {
        "internalType": "string",
        "name": "",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "",
        "type": "string"
      },
      {
        "internalType": "string",
        "name": "",
        "type": "string"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  }
];
const contract_Address = "0x6c3560C2f46C35EeaE8f3f791839C79D593971ac";
const contract = new web3.eth.Contract(abi, contract_Address);


