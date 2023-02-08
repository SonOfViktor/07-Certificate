import {Alert} from '@mui/material';
import {useEffect} from "react";
import {useDispatch} from "react-redux";

const LoadingAlert = ({status, error, cleanErrorAction}) => {
    const dispatch = useDispatch()

    useEffect(() => {
        return () => {
            if (cleanErrorAction && status === 'rejected') {
                dispatch(cleanErrorAction())
            }
        }
    }, [dispatch, cleanErrorAction, status])

    return (
        status === 'rejected' &&
        error && (
            <Alert sx={{fontSize: 16, marginBottom: '10px'}} severity="error">
                {error}
            </Alert>
        )
    );
};

export default LoadingAlert;
