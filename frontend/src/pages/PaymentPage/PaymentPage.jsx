import { Elements } from '@stripe/react-stripe-js';
import React, { useEffect, useState } from 'react'
import CheckoutForm from './CheckoutPayment';
import { loadStripe } from '@stripe/stripe-js';
import { useDispatch, useSelector } from 'react-redux';
import { setLoading } from '../../store/features/common';
import { fetchUserDetails } from '../../api/userInfo';
import { selectCartItems } from '../../store/features/cart';


//Publishable Key
const stripePromise = loadStripe('pk_test_51Sh5ETL3E4yIqr1xlzGtrhwnPsW4sLKwHLVsztd2MGxeAVf2qW2uCqrzgi42RACVCQNheoW1zMbiBHlA9WKCA0ov00bQkxJxtw');

const PaymentPage = (props) => {

    const options = {
        mode: 'payment',
        amount: 100,
        currency: 'inr',
        // Fully customizable with appearance API.
        appearance: {
            theme: 'flat'
        },
    };
    return (
        <div>
            <Elements stripe={stripePromise} options={options}>
                <CheckoutForm {...props}/>
            </Elements>
        </div>
    )
}

export default PaymentPage