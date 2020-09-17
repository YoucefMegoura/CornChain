//SPDX-License-Identifier: MIT
pragma solidity >= 0.5.0 < 0.7.0;

contract SupplyChain{
    
    enum StateValue {
        Oaic, 
        Minoterie, 
        Grossiste, 
        Boulangerie
    }

    struct Product{
        address creator;
        uint256 productId;
        string productName;
        uint256 date;
        StateValue state;
        string temp1;
        string hum1;
        string temp2;
        string hum2;
    }
    mapping(uint => Product) public allProducts;
    Product public product;
    uint256 public index;

    // Events
    event oaicCreated(uint256 index);
    event minoterieSeted(bool response);
    event grossisteSeted(bool response);
    event boulangerieSeted(bool response);

    constructor () public {
        // We begin with that number juste for the demonstration purposes 
        index = 256452170; 
    }

    // Create a new product from the OAIC
    function newItem(string memory _productName) public {
        product = Product( msg.sender, index, _productName, now, StateValue.Oaic, "0", "0", "0", "0" );
        emit oaicCreated(index);
        allProducts[index] = product;
        index = index + 1;
    }
    

    function minoterieSet(uint256 _productId, string memory _temp1, string memory _hum1) public {
        allProducts[_productId].temp1 = _temp1;
        allProducts[_productId].hum1 = _hum1;
        setState(_productId, StateValue.Minoterie);
        emit minoterieSeted(true);
    }

    function grossisteSet(uint256 _productId, string memory _temp2, string memory _hum2) public {
        allProducts[_productId].temp2 = _temp2;
        allProducts[_productId].hum2 = _hum2;
        setState(_productId, StateValue.Grossiste);
        emit grossisteSeted(true);
    }

    function boulangerieSet(uint256 _productId) public {
        setState(_productId, StateValue.Boulangerie);
        emit boulangerieSeted(true);
    }

    
    function setState(uint256 _productId, StateValue _state) public {
        allProducts[_productId].state = _state;
    }
    
    function getProduct(uint256 _productId) public view returns (
            address, 
            uint256, 
            string memory, 
            uint256, 
            StateValue, 
            string memory, 
            string memory, 
            string memory, 
            string memory){
            require(_productId < index, "Error: index : out of band ");
            Product memory p = allProducts[_productId];
        return (
                p.creator,
                p.productId,
                p.productName,
                p.date,
                p.state, 
                p.temp1,
                p.hum1,
                p.temp2,
                p.hum2
            );
    }   
}