describe('My first test', function () {    it('Visit Rentdeck all properties page', function () {        cy.on('uncaught:exception', (err, runnable) => {            return false        });        cy.visit('http://localhost:8081/');        cy.contains('All properties').click();        cy.url()            .should('contain', '/all')    });    // it('Can see a property', function () {    //     cy.on('uncaught:exception', (err, runnable) => {    //         return false    //     });    //     cy.visit('http://localhost:8081/');    //     cy.contains('All properties').click();    //    //     cy.contains('Best room for best price').click();    //    //     // cy.url()    //     //     .should('contain', '/room/2')    // });    it('Can do login', function () {        cy.on('uncaught:exception', (err, runnable) => {            return false        });        cy.visit('http://localhost:8081/');        cy.contains('Login').click();        cy.get('#formUsername')            .type('jj@toores.ee')        cy.get('#formPassword')            .type('admin')        cy.get('form').contains('Login').click()        cy.contains('My properties').click();        cy.url()            .should('contain', '/my/rooms')    });    // it('Can add property', function () {    //     cy.on('uncaught:exception', (err, runnable) => {    //         return false    //     });    //     cy.visit('http://localhost:8081/');    //     cy.contains('Login').click();    //    //     cy.get('#formUsername')    //         .type('jj@toores.ee')    //    //     cy.get('#formPassword')    //         .type('admin')    //    //     cy.get('form').contains('Login').click()    //     cy.contains('Add property').click();    //    //     cy.get('input').contains('Name of your property')    //         .type('This is test property')    //         .should('This is test property')    //    //    // });})