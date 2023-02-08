import React from 'react';
import FormHat from '../components/ui/FormHat';
import Bucket from '../components/checkout/Bucket';

const CheckoutPage = () => {
  return (
    <main>
      <FormHat title="Checkout" />
      <Bucket />
    </main>
  );
};

export default CheckoutPage;
