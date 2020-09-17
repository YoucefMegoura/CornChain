const express = require('express')

const router = express.Router()

const connectSmartContract = require("../utils/connect-smartcontract")

router.post('/grossiste', (req, res, next) => {
    let productId = Number(req.body.productId)
    let temp2 = req.body.temp2
    let hum2 = req.body.hum2

    connectSmartContract.grossisteSet(productId, temp2, hum2)
        .then((result) => {
            return result.events.grossisteSeted.returnValues.response
        })
        .then((result) => {
            res.status(200)
            .send({
                "Grossiste response" : "ok",
                "result" : result
            })
        })
        .catch((e) => console.log("Grossiste. Something went wrong + ", e))

})


module.exports = router