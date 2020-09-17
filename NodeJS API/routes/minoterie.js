const express = require('express')

const router = express.Router()

const connectSmartContract = require("../utils/connect-smartcontract")

router.post('/minoterie', (req, res, next) => {
    let productId = Number(req.body.productId)
    let temp1 = req.body.temp1
    let hum1 = req.body.hum1

    connectSmartContract.minoterieSet(productId, temp1, hum1)
        .then((result) => {
            return result.events.minoterieSeted.returnValues.response
        })
        .then((result) => {
            res.status(200)
            .send({
                "Minoterie response" : "ok",
                "result" : result
            })
        })
        .catch((e) => console.log("Minoterie. Something went wrong + ", e))

})


module.exports = router