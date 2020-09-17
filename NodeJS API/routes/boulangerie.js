const express = require('express')

const router = express.Router()

const connectSmartContract = require("../utils/connect-smartcontract")

router.post('/boulangerie', (req, res, next) => {
    let productId = Number(req.body.productId)

    connectSmartContract.boulangerieSet(productId)
        .then((result) => {
            return result.events.boulangerieSeted.returnValues.response
        })
        .then((result) => {
            res.status(200)
            .send({
                "Boulangerie response" : "ok",
                "result" : result
            })
        })
        .catch((e) => console.log("Boulangerie. Something went wrong + ", e))
})


module.exports = router