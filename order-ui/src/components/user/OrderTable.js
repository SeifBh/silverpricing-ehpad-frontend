import React, { Fragment } from 'react'
import { Grid, Table, Header, Icon } from 'semantic-ui-react'
import CreateOrderForm from '../misc/CreateOrderForm'

function OrderTable({ orders, orderDescription, handleChange, createOrder }) {
  let orderList
  if (!orders || orders.length === 0) {
    orderList = (
      <Table.Row key='no-order'>
        <Table.Cell collapsing textAlign='center' colSpan='3'>No order</Table.Cell>
      </Table.Row>
    )
  } else {
    orderList = orders.map(order => {
      return (
        <Table.Row key={order.id}>
          <Table.Cell>{order.id}</Table.Cell>
          <Table.Cell>{order.createdAt}</Table.Cell>
          <Table.Cell>{order.description}</Table.Cell>
        </Table.Row>
      )
    })
  }

  return (
    <Fragment>
      <Grid stackable divided>
        <Grid.Row columns='2'>
          <Grid.Column width='3'>
            <Header as='h2'>
              <Icon name='laptop' />
              <Header.Content>Orders</Header.Content>
            </Header>
          </Grid.Column>
          <Grid.Column>
            <CreateOrderForm
              orderDescription={orderDescription}
              handleChange={handleChange}
              createOrder={createOrder}
            />
          </Grid.Column>
        </Grid.Row>
      </Grid>

      <Table compact striped>
        <Table.Header>
          <Table.Row>
            <Table.HeaderCell width={5}>ID</Table.HeaderCell>
            <Table.HeaderCell width={5}>Created At</Table.HeaderCell>
            <Table.HeaderCell width={6}>Description</Table.HeaderCell>
          </Table.Row>
        </Table.Header>
        <Table.Body>
          {orderList}
        </Table.Body>
      </Table>
    </Fragment>
  )
}

export default OrderTable