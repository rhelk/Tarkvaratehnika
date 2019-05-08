describe('My first test', function () {
    beforeEach(() => {
        cy.on('uncaught:exception', (err, runnable) => {
            return false
        });
        cy.visit('http://localhost:8081/');
        cy.contains('Login').click();

        cy.get('#formUsername')
            .type('jj@toores.ee')

        cy.get('#formPassword')
            .type('admin')

        cy.get('form').contains('Login').click()
    })

    // it('Can see a property', function () {
    //     cy.on('uncaught:exception', (err, runnable) => {
    //         return false
    //     });
    //     cy.visit('http://localhost:8081/');
    //     cy.contains('All properties').click();
    //
    //     cy.contains('Best room for best price').click();
    //
    //     // cy.url()
    //     //     .should('contain', '/room/2')
    // });

    it('Can login', function () {

        cy.url().should('equal', 'http://localhost:8081/')

    });

    it('Can go under My properties page', function () {


        cy.contains('My properties').click();

        cy.url()
            .should('contain', '/my/rooms')

    });

    it('Can go under My rent request page', function () {


        cy.contains('My rent request').click();

        cy.url().should('contain', '/user')

    });

    it('Can go to Add property page', function () {


        cy.contains('Add property').click();

        cy.url().should('contain', '/property/add')

    });
    //
    // it('Can disable property', function () {
    //     cy.url().should('equal', 'http://localhost:8081/')
    //     cy.contains('All properties').click();
    //
    //     cy.contains('Time of your life')
    //
    //     cy.contains('My properties').click();
    //
    //     cy.contains('Deactivate').click()
    //
    //     cy.contains('All properties').click();
    //
    //     cy.should('not.have.value', 'Time of your life')
    //
    // });

    // it('Can add property', function () {
    //     cy.on('uncaught:exception', (err, runnable) => {
    //         return false
    //     });
    //     cy.visit('http://localhost:8081/');
    //     cy.contains('Login').click();
    //
    //     cy.get('#formUsername')
    //         .type('jj@toores.ee')
    //
    //     cy.get('#formPassword')
    //         .type('admin')
    //
    //     cy.get('form').contains('Login').click()
    //     cy.contains('Add property').click();
    //
    //     cy.get('input').contains('Name of your property')
    //         .type('This is test property')
    //         .should('This is test property')
    //
    //
    // });
})