import {Paper} from '@mui/material';
import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {selectUserState} from '../../store/user/userSelectors';
import SuccessSnackbar from '../ui/SuccessSnackbar';
import LoginForm from './LoginForm';
import BigLogo from './BigLogo';
import SignUpLink from './SignUpLink';
import {useLocation, useNavigate} from 'react-router-dom';
import {cleanUserStatus} from "../../store/user/userSlice";

const containerStyle = {
    position: 'relative',
    width: '300px',
    padding: '150px 50px 50px 50px',
};

const LoginWindow = () => {
    const dispatch = useDispatch();
    const location = useLocation();
    const navigate = useNavigate();
    const {status, error, user} = useSelector(selectUserState);
    const fromPage = location.state?.from?.pathname || '/';

    useEffect(() => {
        if (user) {
            navigate(fromPage);
        }
    }, [user, navigate, fromPage]);

    useEffect(() => {
        if (status === 'created') {
            dispatch(cleanUserStatus())
        }
    }, [dispatch, status])

    return (
        <Paper sx={containerStyle}>
            <BigLogo/>
            <LoginForm status={status} error={error}/>
            <SignUpLink/>
            <SuccessSnackbar
                isShow={status === 'created'}
                message="User was created successfully. You can log in"
            />
        </Paper>
    );
};

export default LoginWindow;
