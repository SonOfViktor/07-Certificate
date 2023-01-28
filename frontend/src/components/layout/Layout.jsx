import React from 'react';
import {Outlet} from 'react-router-dom';
import Footer from '../footer/Footer';
import NavBar from '../header/NavBar';

const Layout = () => {
  return (
    <>
      <header>
        <NavBar />
      </header>
      <Outlet />
      <Footer />
    </>
  );
};

export default Layout;
