import Layout from './../components/layout/Layout';
import SecurityAuth from './../components/provider/SecurityAuth';
import AdminCertificatePage from './../page/AdminCertificatePage';
import CheckoutPage from './../page/CheckoutPage';
import FavoritePage from './../page/FavoritePage';
import ItemDetailsPage from './../page/ItemDetailsPage';
import LoginPage from './../page/LoginPage';
import MainPage from './../page/MainPage';
import NotFoundPage from './../page/NotFoundPage';
import RegisterPage from './../page/RegisterPage';
import UserPage from './../page/UserPage';

export const routes = [
  {
    path: '/',
    element: <Layout />,
    children: [
      {
        index: true,
        element: <MainPage />,
      },
      {
        path: 'details/:id',
        element: <ItemDetailsPage />,
      },
      {
        path: 'login',
        element: <LoginPage />,
      },
      {
        path: 'register',
        element: <RegisterPage />,
      },
      {
        path: 'checkout',
        element: (
          <SecurityAuth>
            <CheckoutPage />
          </SecurityAuth>
        ),
      },
      {
        path: 'favorite',
        element: (
          <SecurityAuth>
            <FavoritePage />
          </SecurityAuth>
        ),
      },
      {
        path: 'user',
        element: (
          <SecurityAuth>
            <UserPage />
          </SecurityAuth>
        ),
      },
      {
        path: 'admin/certificate',
        element: (
          <SecurityAuth role="admin">
            <AdminCertificatePage />
          </SecurityAuth>
        ),
      },
      {
        path: '*',
        element: <NotFoundPage />,
      },
    ],
  },
];
