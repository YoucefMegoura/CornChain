const minoterieRoute = require('./routes/minoterie')
const grossisteRoute = require('./routes/grossiste')
const boulangerieRoute = require('./routes/boulangerie')

const express = require('express')
const path = require('path')

const bodyParser = require('body-parser')

const app = express()
app.set('view engine', 'ejs')

app.use(bodyParser.urlencoded({extended: true}))
app.use(express.static(path.join(__dirname, 'public')))

const connectSmartContract = require('./utils/connect-smartcontract')
connectSmartContract.init() //Init the Web 3 Module

app.use(minoterieRoute)
app.use(grossisteRoute)
app.use(boulangerieRoute)

app.use('/', (req, res, next) => {
    res.status(404)
        .render(
            '404', 
            {
                pageTitle: "404 - Page not found!",
                path: "/"
            }
        )
})

app.listen(3300)